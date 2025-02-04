document.getElementById('registerForm').addEventListener('submit', function(e) {
    e.preventDefault(); // Prevenir el envío normal del formulario

    const username = document.getElementById('registerUsername').value;
    const password = document.getElementById('registerPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    // Verificación de contraseñas
    if (password !== confirmPassword) {
        alert('Las contraseñas no coinciden');
        return;
    }

    // Crear un objeto con los datos para el registro
    const userData = {
        username: username,
        password: password
    };

    // Enviar la solicitud POST al backend
    fetch('http://localhost:8081/app/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',  // Especifica que el cuerpo de la solicitud es JSON
        },
        body: JSON.stringify(userData), // Convertir el objeto JavaScript a un string JSON
    })
    .then(response => response.json()) // Convertir la respuesta en JSON
    .then(data => {
        if (data.token) {
            alert('Usuario registrado con éxito');
            console.log('Token:', data.token); // Ver el token de respuesta en la consola
            // Aquí podrías guardar el token en el almacenamiento local si lo deseas
            localStorage.setItem('token', data.token);
        } else {
            alert('Hubo un problema al registrar el usuario');
        }
    })
    .catch(error => {
        console.error('Error al registrar el usuario:', error);
        alert('Error al registrar el usuario');
    });
});
