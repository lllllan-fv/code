$(document).ready(function () {
    console.log("ready");

    get_user_list();
})

var user_list;

// 获取所有的用户信息
function get_user_list() {

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
            // console.log(data);
            user_list = data.data;
        },
        error: function () {
            alert("sth wrong");
        }
    });

    show_user_list(user_list);
}

function show_user_list(list) {
    var table_data = "";
    console.log("user_list", list);
    for (var idx in list) {
        var item = list[idx];
        // console.log(item);
        var row = "<tr>"
            + "<td class='cctvmid' style='color: gray;'>" + item.user_id + "</td>"
            + "<td class='cctvmid' style='color: gray;'>" + item.user_name + "</td>"
            + "<td class='cctvmid'><img src='" + item.user_img + "' class='img-circle' style='width:30%;'></td>"
            + "<td class='cctvmid' style='color: gray;'>" + item.user_phone + "</td>"
            + "<td class='cctvmid' style='color: gray;'>" + (item.user_per == 1 ? "会员" : "非会员") + "</td>"
            + "<td class='cctvmid'>"
            + "<button type='button' class='btn btn-success' onclick='reverse_permission(" + idx + ")'>权限修改</button>"
            + "<button type='button' class='btn btn-danger' onclick='reset_pwd(" + item.user_id + ")'>密码重置</button>"
            + "</td></tr>";

        // console.log(row);
        table_data += row;
    }

    $("tbody").html(table_data);
}

// 修改单个用户的阅读权限
function reverse_permission(idx) {
    console.log("click reverse permission");

    var user_id = user_list[idx].user_id;
    var permission = (user_list[idx].user_per ^= 1);
    console.log(user_id, permission);

    $.ajax({
        url: 'http://online_library.lllllan.cn/api/user/edit_per.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
            "user_id": user_id,
            "per": permission,
        },
        success: function (data) {
            console.log(data);
        },
        error: function () {
            alert("sth wrong");
        }
    });

    show_user_list(user_list);
}

// 重置用户的密码
function reset_pwd(user_id) {
    console.log(user_id);

    $.ajax({
        url: 'http://online_library.lllllan.cn/api/user/reset_pwd.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "phone": "12345678900",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIkcGhvbmUiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTY1MjcwNjgzMCwianRpIjoiZmI2NDIxMDQwNDJkNmQyMzkyOGVjODNhYWQ4OTNiMTMifQ.ULK9s4sbmwPwYlVh94uU66LkcQ5kPe0hnGqeIcZPG2Y",
            "user_id": user_id,
        },
        success: function (data) {
            console.log(data);
        },
        error: function () {
            alert("sth wrong");
        }
    });
}

function search() {
    console.log("click search");
    console.log($("#name").val());

    var key = $("#name").val();

    var list = [];
    for (var idx in user_list) {
        var item = user_list[idx];
        if (item.user_name.includes(key) || item.user_phone.includes(key)) {
            list.push(item);
        }
    }

    show_user_list(list);
}