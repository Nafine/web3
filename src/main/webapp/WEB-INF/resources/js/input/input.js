let validX = true;
let validY = false;
let validR = true;

$('input[name="x"]').on('keyup', function () {
    let x = $(this).val();
    validX = isValidX(parseFloat(x));
    if (!x.match($(this).prop('pattern')) || !validX) {
        $('#x-err').addClass('show');
        validX = false;
    } else {
        $('#x-err').removeClass('show');
        validX = true;
    }
    checkInput();
});

$('input[name="y"]').on('keyup', function () {
    let y = $(this).val();
    validY = isValidY(parseFloat(y));
    if (!y.match($(this).prop('pattern')) || !validY) {
        $('#y-err').addClass('show');
        validY = false;
    } else {
        $('#y-err').removeClass('show');
        validY = true;
    }
    checkInput();
})

function isValidY(y) {
    return y != null && !isNaN(y) && y >= -5 && y <= 3;
}

function isValidX(x) {
    return x != null && !isNaN(x) && x >= -5 && x <= 5;
}

$('input[name="r"]').on('change', function () {
    let radius = parseFloat($(this).val());
    refresh(radius);
});

function checkInput() {
    if (validX && validY && validR) {
        $('#submit').prop('disabled', false);
    } else {
        $('#submit').prop('disabled', true);
    }
    if (validR) {
        $('#clear-points').prop('disabled', false);
    } else {
        $('#clear-points').prop('disabled', true);
    }
}

$('#requestTable tbody').on('click mouseenter mouseleave', 'tr', function (event) {
    let row = $(this).children();

    let x = parseFloat(row.eq(0).text());
    let y = parseFloat(row.eq(1).text());

    if (event.type === 'mouseenter') {
        drawDot({x: x, y: y}, 'red');
    } else if (event.type === 'mouseleave') {
        refresh(parseFloat($('input[name="r"]:checked').val()));
    }
});

function getCursorPosition(canvas, event) {
    const rect = canvas.getBoundingClientRect();
    const x = (event.clientX - rect.left - 250) / 200;
    const y = (event.clientY - rect.top - 250) / -200;
    return {x: x, y: y};
}

$('#graph').on('click', function (e) {
    let pos = getCursorPosition(canvas, e);
    if (validR) {
        let r = parseFloat($('input[name="r"]:checked').val());
        sendData(pos.x * r, pos.y * r, r).then(updateCurrent);
    }
})