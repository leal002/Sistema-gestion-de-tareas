document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    // Lógica de autenticación: enviar al backend con fetch
    fetch('http://localhost:8081/app/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.token) {
            alert('Login exitoso');
            window.location.href = 'tareas.html'; // Redirigir a la página de tareas
            console.log('Token:', data.token);
            // Aquí puedes guardar el token, por ejemplo, en localStorage
            localStorage.setItem('token', data.token);
        } else {
            alert('Error en el login');
        }
    })
    .catch(error => {
        console.error('Error en la solicitud:', error);
        alert('Hubo un error al intentar iniciar sesión');
    });
});



