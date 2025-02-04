// Variables para los elementos del formulario
const createTaskForm = document.getElementById("createTaskForm");
const taskList = document.getElementById("taskList");

// Función para obtener todas las tareas
function getTasks() {
    fetch('http://localhost:8081/app/tasks')
        .then(response => response.json())
        .then(tasks => {
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
        .catch(error => console.error('Error al obtener tareas:', error));
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
    const newTitle = prompt("Nuevo título de la tarea:");
    const newDescription = prompt("Nueva descripción:");
    let newDueDate = prompt("Nueva fecha de vencimiento (YYYY-MM-DD HH:MM:ss):");
    const newUsuarioid = prompt("Nuevo ID de usuario:");

    if (!newTitle || !newDescription || !newDueDate) {
        alert("Todos los campos son obligatorios");
        return;
    }

    if (!newDueDate.includes("T")) {
        newDueDate = newDueDate.replace(" ", "T");  // Reemplazar el espacio por "T"
    }

    const newStatus = confirm("¿La tarea está completada?");

    

    const updatedTask = {
        titulo: newTitle,
        descripcion: newDescription,
        fechaVencimiento: newDueDate,  // Mantener formato correcto
        completada: newStatus,
        usuario: { id: newUsuarioid }
    };

    fetch(`http://localhost:8081/app/${taskId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedTask)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error al actualizar tarea: ${response.status}`);
        }
        alert("Tarea actualizada correctamente");
        getTasks(); // Recargar lista
    })
    .catch(error => console.error('Error al actualizar tarea:', error));
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
    fetch('http://localhost:8081/app/users')
        .then(response => response.json())
        .then(usuarios => {
            const userSelect = document.getElementById('userId');
            userSelect.innerHTML = '<option value="" disabled selected>Selecciona un usuario</option>'; // Limpiar opciones anteriores
            usuarios.forEach(usuario => {
                const option = document.createElement('option');
                option.value = usuario.id; // Asignar el ID de usuario al valor
                option.textContent = usuario.nombre; // Mostrar el nombre del usuario
                userSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error al obtener usuarios:', error));
}

// Cargar tareas y usuarios al iniciar
window.onload = function () {
    getTasks();  // Para cargar las tareas existentes
    getUsuarios();  // Para cargar los usuarios en el formulario
};
