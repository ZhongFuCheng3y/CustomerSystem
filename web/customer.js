/**
 *
 * Created by ozc on 2017/2/27.
 */

function pageInit() {
    makeYear();
    makeMonth();
    makeDay();
}



function makeForm() {

    makeBirthday();
    makePreference();
    return true;

}
function makeBirthday() {

    //获取下拉框的数据，把数据拼凑成日期字符串
    var year = document.getElementById("year").value;
    var month = document.getElementById("month").value;
    var day = document.getElementById("day").value;
    var birthday = year + "-" + month + "-" + day;


    //想要将拼凑完的字符串提交给服务器，用隐藏域就行了
    var input = document.createElement("input");
    input.type = "hidden";
    input.value = birthday;
    input.name = "birthday";

    //将隐藏域绑定在form下【为了方便，在form中设置id，id名字为form】
    document.getElementById("form").appendChild(input);

}

function makePreference() {

    //获取喜好的控件
    var hobbies = document.getElementsByName("hobbies");

    //定义变量，记住用户选中的选项
    var preference = "";

    //遍历喜好的控件，看用户选上了什么！
    for (var i = 0; i < hobbies.length; i++) {

        if (hobbies[i].checked == true) {
            preference += hobbies[i].value + ",";
        }
    }

    //刚才拼凑的时候，最后一个逗号是多余的，我们要把它去掉
    preference = preference.substr(0, preference.length - 1);

    //也是用隐藏域将数据带过去给服务器
    var input = document.createElement("input");
    input.type = "hidden";
    input.value = preference;
    input.name = "preference";

    //将隐藏域绑定到form表单上
    document.getElementById("form").appendChild(input);

}
function makeYear() {

    //得到下拉框的控件
    var year = document.getElementById("year");

    //要想下拉框有更多的数据，就需要有更多的option控件
    //js获取得到年份是getFullYear()，单单的getYear()只是获取两位数
    for (var i = 1901; i <= new Date().getFullYear(); i++) {

        //生成option控件
        var option = document.createElement("option");

        //option控件的值和文本内容为循环生成的年分！
        option.value = i;
        option.innerText = i;

        //将生成option控件绑定到select控件上
        year.appendChild(option);
    }

}

function makeMonth() {
    var month = document.getElementById("month");
    for (var i = 2; i <= 12; i++) {
        var option = document.createElement("option");
        if (i < 10) {
            option.value = '0' + i;
            option.innerText = '0' + i;
        } else {
            option.value = i;
            option.innerText = i;
        }
        month.appendChild(option);
    }
}

function makeDay() {
    var day = document.getElementById("day");
    for (var i = 2; i <= 12; i++) {
        var option = document.createElement("option");
        if (i < 10) {
            option.value = '0' + i;
            option.innerText = '0' + i;
        } else {
            option.value = i;
            option.innerText = i;
        }
        day.appendChild(option);
    }
}

