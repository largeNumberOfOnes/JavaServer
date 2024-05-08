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



// async function sendSendRequest() {
//     let url = '/public/smallPage.html';
//     let response = await fetch(url, {
//         method: 'GET'
//     });
    
//     if (response.ok) {
//         let ans = await response.text();
//         resp = response;
//         // globans = ans;
//         // console.log(ans);
//         // window.location.href = response.headers.get('Redirect');
//         // window.location.replace(response.headers.get('Redirect'));
//     } else {
//         console.log('response is not ok')
//         console.log("Ошибка HTTP: " + response.status);
//     }
// }

async function sendRegisterRequest() {

    let name  = document.getElementById('usernameid').value;
    let pass  = document.getElementById('userpassid').value;
    // console.log("user name: " + name);
    // console.log("user pass: " + pass);
    // return;

    let url = '/public/users/register';
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

let resp;
async function sendLoginRequest() {
    let url = '/public/users/login';
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
        // let ans = await response;
        resp = response;
        // console.log('--------------');
        // console.log(response.headers);
        let cookieList = response.headers.get('SetSessionIdentifier').split('; ');
        cookieList.forEach(function(item, i, arr) {
            document.cookie = item;
        });
          // alert( i + ": " + item + " (массив:" + arr + ")" );
          // el = item.split('=');
          // if (el[0] == 'sessionIdentifier') {
          //   doel[1];
          // }
        // globans = ans;
        // console.log(ans);
        // window.location.href = response.headers.get('Redirect');
        window.location.replace(response.headers.get('Redirect'));
    } else {
        console.log('response is not ok')
        console.log("Ошибка HTTP: " + response.status);
    }
}

function deleteAllCookies() {
    const cookies = document.cookie.split(";");

    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i];
        const eqPos = cookie.indexOf("=");
        const name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=; Max-Age=-99999999;";
    }
}

// ------------------------------------------------------------------------------------------

async function sendGetMesRequest() {
    let url = '/internal/users/getmes';
    let response = await fetch(url, {
        method: 'GET'
    }
    );
    let mesList = await response.text();
    // let mesList = response.text();
    // console.log(mesList);
    // console.log('w;jgwen');
    document.getElementById('e217281728').innerHTML = mesList;
    
    if (response.ok) {
        
    } else {
        console.log('response is not ok')
        console.log("Ошибка HTTP: " + response.status);
    }
}

function getMes() {
    return document.getElementById('sendField00').value; 
}

async function sendSendRequest() {
    let url = '/internal/users/send';
    let response = await fetch(url, {
        method: 'POST',
        headers: {
            mes: getMes()
        }
    }
    );
    
    if (response.ok) {
        console.log('OK');
    } else {
        console.log('response is not ok')
        console.log("Ошибка HTTP: " + response.status);
    }
}
