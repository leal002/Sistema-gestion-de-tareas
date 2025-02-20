let userId = null; // Guardará el ID del usuario autenticado

function loadUserProfile() {
    fetch("http://localhost:8081/app/autenticado", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token"),
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error en la autenticación");
        }
        return response.json();
    })
    .then(data => {
        userId = data.id;
        document.getElementById("userName").textContent = data.username;
        document.getElementById("userRole").textContent = data.role;
    })
    .catch(error => console.error("Error al obtener perfil:", error));
}

function editProfile() {
    fetch("http://localhost:8081/app/autenticado", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token"),
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("No se pudo obtener el perfil");
        }
        return response.json();
    })
    .then(data => {
        document.getElementById("name").value = data.username;
        document.getElementById("role").value = data.role;
        document.getElementById("currentPassword").value = ""; // Limpia campo
        document.getElementById("newPassword").value = ""; // Limpia campo
        document.getElementById("profileModal").style.display = "block";
    })
    .catch(error => console.error("Error al obtener perfil:", error));
}



function verifyAndSaveProfile() {
    const currentPassword = document.getElementById("currentPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const username = document.getElementById("name").value;
    const role = document.getElementById("role").value;
    const errorMessage = document.getElementById("errorMessage");

    console.log("Iniciando verificación de contraseña...");

    fetch("http://localhost:8081/app/verificarcontraseña", {  // 🔹 Corrige la URL
        method: "POST",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token"),
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ password: currentPassword })
    })
    .then(response => {
        console.log("Respuesta de verificación:", response.status);
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text || "Error en la verificación de contraseña"); });
        }
        return response.json().catch(() => ({})); // 🔹 Maneja respuesta vacía
    })
    .then(() => {
        console.log("Contraseña verificada correctamente. Actualizando perfil...");

        const updatedProfile = {
            currentPassword,  // 🔹 Ahora `UserDTO` lo tiene
            username,         // 🔹 Se incluye `username`
            password: newPassword || null,  
            role
        };

        return fetch("http://localhost:8081/app/actualizarperfil", {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token"),
                "Content-Type": "application/json"
            },
            body: JSON.stringify(updatedProfile)
        });
    })
    .then(response => {
        console.log("Respuesta de actualización:", response.status);
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text || "Error al actualizar el perfil"); });
        }
        return response.json().catch(() => ({})); // 🔹 Maneja respuesta vacía
    })
    .then(() => {
        alert("Perfil actualizado con éxito");
        closeModal();
        loadUserProfile();
        window.location.href = 'index.html';
    })
    .catch(error => {
        console.error("Error en la actualización del perfil:", error);
        errorMessage.textContent = error.message;
        errorMessage.style.display = "block";
    });
}



function closeModal() {
    document.getElementById("profileModal").style.display = "none";
}


// Cargar perfil del usuario al cargar la página
window.onload = loadUserProfile;
