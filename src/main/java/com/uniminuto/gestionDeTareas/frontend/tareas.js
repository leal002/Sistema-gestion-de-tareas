document.addEventListener("DOMContentLoaded", function () {
    console.log("Cargando tareas y usuarios...");
    getTasks();
    getUsuarios();
});

// Variables para los elementos del formulario
const createTaskForm = document.getElementById("createTaskForm");
const taskList = document.getElementById("taskList");

// Función para obtener todas las tareas
function getTasks() {
    const token = localStorage.getItem("token"); // Obtener token de autenticación

    if (!token) {
        console.error("⚠️ No hay token en localStorage. Asegúrate de haber iniciado sesión.");
        return;
    }

    fetch('http://localhost:8081/app/tasks', {
        method: 'GET',
        headers: {
            "Authorization": "Bearer " + token,  // Se añade el token de autenticación
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP ${response.status} - Acceso denegado o error en el servidor`);
        }
        return response.json();
    })
    .then(tasks => {
        const taskList = document.getElementById('taskList'); // Asegurar que existe
        if (!taskList) {
            console.error("❌ Elemento taskList no encontrado en el DOM.");
            return;
        }
        
        taskList.innerHTML = ''; // Limpiar lista
        tasks.forEach(task => {
            const li = document.createElement('li');
            li.innerHTML = `
                <strong>${task.titulo}</strong> - ${task.completada ? '✅ Completada' : '⏳ Pendiente'}
<button onclick="updateTask(${task.id})">✏️ Editar</button>
                <button onclick="deleteTask(${task.id})">❌ Eliminar</button>
            `;
            taskList.appendChild(li);
        });
    })
    .catch(error => console.error('❌ Error al obtener tareas:', error));
}

// Función para eliminar una tarea
function deleteTask(taskId) {
    if (confirm("¿Seguro que deseas eliminar esta tarea?")) {
        fetch(`http://localhost:8081/app/${taskId}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error al eliminar tarea: ${response.status}`);
            }
            alert("Tarea eliminada correctamente");
            getTasks(); // Recargar lista
        })
        .catch(error => console.error('Error al eliminar tarea:', error));
    }
}

// Función para actualizar una tarea
function updateTask(taskId) {
    fetch(`http://localhost:8081/app/${taskId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error al obtener tarea: ${response.status}`);
            }
            return response.json();
        })
        .then(task => {
            // Llamar a getUsuarios y esperar a que termine antes de continuar
            getUsuarios().then(() => {
                document.getElementById("editTaskId").value = taskId;
                document.getElementById("editTitle").value = task.titulo || "";
                document.getElementById("editDescription").value = task.descripcion || "";
                document.getElementById("editDueDate").value = task.fechaVencimiento ? task.fechaVencimiento.replace(" ", "T") : "";
                document.getElementById("editStatus").value = task.completada ? "true" : "false";

                // Asegurarse de que el usuario de la tarea se seleccione en el dropdown
                setTimeout(() => {
                    document.getElementById("editUserId").value = task.usuario ? task.usuario.id : "";
                }, 500);

                document.getElementById("editTaskModal").style.display = "block";
            });
        })
        .catch(error => console.error("Error al obtener tarea:", error));
}



function saveTaskChanges() {
    const taskId = document.getElementById("editTaskId").value;
    const newTitle = document.getElementById("editTitle").value;
    const newDescription = document.getElementById("editDescription").value;
    let newDueDate = document.getElementById("editDueDate").value;
    const newStatus = document.getElementById("editStatus").value === "true";
    const newUsuarioid =  parseInt(document.getElementById('editUserId').value);

    if (!newTitle || !newDescription || !newDueDate) {
        alert("Todos los campos son obligatorios");
        return;
    }

    // Asegurar el formato correcto de fecha
    if (!newDueDate.includes("T")) {
        newDueDate = newDueDate.replace(" ", "T");
    }

    const updatedTask = {
        titulo: newTitle,
        descripcion: newDescription,
        fechaVencimiento: newDueDate,
        completada: newStatus,
        usuario: { id: newUsuarioid }
    };

    fetch(`http://localhost:8081/app/${taskId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(updatedTask)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error al actualizar tarea: ${response.status}`);
        }
        alert("Tarea actualizada correctamente");
        closeEditModal();
        getTasks(); // Recargar lista de tareas
    })
    .catch(error => console.error("Error al actualizar tarea:", error));
}

function closeEditModal() {
    document.getElementById("editTaskModal").style.display = "none";
}



// Función para crear una nueva tarea
createTaskForm.addEventListener('submit', function (e) {
    e.preventDefault(); // Prevenir el envío normal del formulario

    const titulo = document.getElementById('title').value;
    const descripcion = document.getElementById('description').value;
    let fechaVencimiento = document.getElementById('dueDate').value;

    // Asegúrate de que el formato de la fecha sea correcto (YYYY-MM-DDTHH:MM)
    const formattedDate = fechaVencimiento.length === 16 ? fechaVencimiento + ":00" : fechaVencimiento;

    const completada = document.getElementById('status').value === "true";
    const usuarioId = parseInt(document.getElementById('userId').value); // Obtener el ID del usuario seleccionado

    if (isNaN(usuarioId)) {
        alert("Debe seleccionar un usuario válido");
        return;
    }

    const taskData = {
        titulo,
        descripcion,
        fechaVencimiento: formattedDate, // Formato adecuado para el backend
        completada,
        usuario: { id: usuarioId }  // Usar el ID del usuario seleccionado
    };

    fetch('http://localhost:8081/app/createTask', {  // Asegúrate de que esta URL sea correcta para crear tareas
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(taskData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error en la solicitud: ${response.status}`);
        }
        return response.json();
    })
    .then(task => {
        alert('Tarea creada con éxito');
        getTasks(); // Actualizar lista de tareas
        createTaskForm.reset(); // Limpiar formulario después de crear la tarea
    })
    .catch(error => {
        console.error('Error al crear tarea:', error);
        alert('Error al crear tarea');
    });
});

// Función para obtener todos los usuarios
function getUsuarios() {
    return fetch('http://localhost:8081/app/users')
        .then(response => response.json())
        .then(usuarios => {
            const userSelects = document.querySelectorAll('#userId, #editUserId'); // Obtener ambos selects

            userSelects.forEach(select => {
                select.innerHTML = '<option value="" disabled selected>Selecciona un rol y usuario</option>';
                usuarios.forEach(usuario => {
                    let nombre = usuario.username || "Desconocido";
                    let rol = usuario.role || "Sin rol";
                    const option = document.createElement('option');
                    option.value = usuario.id;
                    option.textContent = `${rol} - ${nombre}`;
                    select.appendChild(option);
                });
            });
        })
        .catch(error => console.error('Error al obtener usuarios:', error));
}

function logout() {
    localStorage.removeItem("token"); // Elimina el token de autenticación
    window.location.href = "index.html"; // Redirige a la página de inicio
}


