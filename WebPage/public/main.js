async function main() {
    console.log('---');
    // let url = 'https://ru.stackoverflow.com/questions/868731/response-json-is-not-a-function';
    // let url = '127.0.0.1:8080/index.html';
    // let url = 'https://habr.com/ru/companies/macloud/articles/553826/';
    let url = 'public/smallPage.html';
    let response = await fetch(url);
    
    if (response.ok) {
        let ans = await response.text();
        console.log(ans);
    } else {
        console.log('response is not ok')
        console.log("Ошибка HTTP: " + response.status);
    }
}
main()



async function sendRegisterRequest() {

    let name  = document.getElementById('usernameid').value;
    let pass  = document.getElementById('userpassid').value;
    // console.log("user name: " + name);
    // console.log("user pass: " + pass);
    // return;

    let url = '/users/register';
    let response = await fetch(url, {
        method: 'POST',
        headers: {
            'login': name,
            'password': pass
        }
    });
    
    if (response.ok) {
        let ans = await response.text();
        console.log(ans);
    } else {
        console.log('response is not ok')
        console.log("Ошибка HTTP: " + response.status);
    }
}

async function sendLoginRequest() {
    let url = '/users/login';
    let response = await fetch(url, {
        method: 'POST',
        headers: {
            'action': 'login',
            'login': document.getElementById('usernameid').value,
            'password': document.getElementById('userpassid').value
        },
        // credentials: "same-origin"
    });
    
    if (response.ok) {
        let ans = await response.text();
        // globans = ans;
        // console.log(ans);
        // window.location.href = response.headers.get('Redirect');
        window.location.replace(response.headers.get('Redirect'));
    } else {
        console.log('response is not ok')
        console.log("Ошибка HTTP: " + response.status);
    }
}

