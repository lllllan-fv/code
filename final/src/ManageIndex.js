$(document).ready(function () {
    console.log("ready");

    get_book_tot();
    get_opinion_tot();
    get_user_tot();
})

function get_user_tot() {
    var user_tot = 0;

    $.ajax({
        url: 'http://online_library.lllllan.cn/api/user/list.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
        },
        success: function (data) {
            console.log(data);
            user_tot = data.data.length;
        },
        error: function () {
            alert("sth wrong");
        }
    });

    $("#user_tot").html(user_tot);
}

function get_opinion_tot() {
    var opinion_tot = 0;

    $.ajax({
        url: 'http://online_library.lllllan.cn/api/opinion/list.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
            "type": '1'
        },
        success: function (data) {
            console.log(data);
            opinion_tot = data.data.length;
        },
        error: function () {
            alert("sth wrong");
        }
    });

    $("#opinion_tot").html(opinion_tot);
}

function get_book_tot() {
    var book_tot = 0;

    $.ajax({
        url: 'http://online_library.lllllan.cn/api/book/list.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
        },
        success: function (data) {
            console.log(data);
            book_tot = data.data.length;
        },
        error: function () {
            alert("sth wrong");
        }
    });

    $("#book_tot").html(book_tot);
}