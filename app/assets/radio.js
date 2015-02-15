$(document).ready(function () {
    $(".text").hide();
    $(".cal").hide();
    $(".check_text").hide();
    $(".days_text").hide();
    $("#r1").click(function () {
        $(".cal").show();
        $(".text").hide();
    });
    $("#r2").click(function () {
        $(".text").show();
        $(".cal").hide();
    });
    $("#recurring_status").click(function () {
        $(".check_text").show();
        $(".days_text").show();
    });
    $('#MyForm input').on('change', function () {
        gender = ('input[type="radio"]:checked').val();
    });
});
