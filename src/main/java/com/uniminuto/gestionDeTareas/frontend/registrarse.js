document.addEventListener("DOMContentLoaded", function () {
    cargarRoles(); // Llama a la función para llenar el select con los roles
});

function cargarRoles() {
    fetch("http://localhost:8081/app/roles") // Ajusta la URL del backend
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al obtener los roles");
            }
            return response.json();
        })
        .then(roles => {
            const selectRol = document.getElementById("registerRol");
            selectRol.innerHTML = '<option value="">Seleccione un rol</option>'; // Opción por defecto

            roles.forEach(role => {
                const option = document.createElement("option");
                option.value = role;
                option.textContent = role;
                selectRol.appendChild(option);
            });
        })
        .catch(error => console.error("Error al cargar los roles:", error));
}

document.getElementById("registerForm").addEventListener("submit", function (e) {
    e.preventDefault(); // Evita el envío normal del formulario

    const username = document.getElementById("registerUsername").value;
    const password = document.getElementById("registerPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const rol = document.getElementById("registerRol").value;

    // Verificación de contraseñas
    if (password !== confirmPassword) {
        alert("Las contraseñas no coinciden");
        return;
    }

    if (!rol) {
        alert("Por favor, selecciona un rol");
        return;
    }

    // Crear un objeto con los datos para el registro
    const userData = { username, password, rol };

    // Enviar la solicitud POST al backend
    fetch("http://localhost:8081/app/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al registrar el usuario");
            }
            return response.json();
        })
        .then(data => {
            if (data.token) {
                alert("Usuario registrado con éxito");
                console.log("Token:", data.token);
                localStorage.setItem("token", data.token);
                window.location.href = 'index.html';
            } else {
                alert("Hubo un problema al registrar el usuario");
            }
        })
        .catch(error => {
            console.error("Error al registrar el usuario:", error);
            alert("Error al registrar el usuario");
        });
});

