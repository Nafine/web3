$(handleR);

function handleR() {
    let radius = parseFloat(document.querySelector('[name$=":r-input"]').value);
    refresh(radius);
}

function getCursorPosition(canvas, event) {
    const rect = canvas.getBoundingClientRect();
    const x = (event.clientX - rect.left - 250) / 200;
    const y = (event.clientY - rect.top - 250) / -200;
    const r = parseFloat(document.querySelector('[name$=":r-input"]').value);
    return {x: x * r, y: y * r};
}

$(function () {
    const xInput = document.querySelector('[name$=":x-hidden"]');
    const xDisplay = document.querySelector('[id$=":x-display"]');
    const yInput = document.querySelector('[name$=":y-input"]');

    xInput.value = '1.0';
    yInput.value = '1.0';
    xDisplay.textContent = 'X: 1.0';
});

$(function () {
    const yInput = document.querySelector('[name$=":y-input"]');
    yInput.oninput = event => {
        const y = parseFloat(yInput.value);
        if (!(yInput.value).match(/^-?\d+\.?\d*$/gm) || y < -3 || y > 3 || event.key === '-') {
            $('#y-err').addClass('show');
        } else{
            $('#y-err').removeClass('show');
        }
    }
});

$('#graph').on('click', function (e) {
    let pos = getCursorPosition(canvas, e);

    const xInput = document.querySelector('[name$=":x-hidden"]');
    const xDisplay = document.querySelector('[id$=":x-display"]');
    const yInput = document.querySelector('[name$=":y-input"]');

    const xPrev = xInput.value;
    const yPrev = yInput.value;

    if (xInput) xInput.value = pos.x;
    if (xDisplay) xDisplay.textContent = 'X: ' + pos.x;

    if (yInput) yInput.value = pos.y;

    document.querySelector('[id$=":form-submit"]').click();

    if (xInput) xInput.value = xPrev;
    if (yInput) yInput.value = yPrev;
    if (xDisplay) xDisplay.textContent = 'X: ' + xPrev;
})

$('main').on('click mouseenter mouseleave', '#requestTable tbody tr', function (event) {
    let row = $(this).children();

    const x = parseFloat(row.eq(0).text());
    const y = parseFloat(row.eq(1).text());
    let hit = row.eq(3).text() === "true";

    if (event.type === 'mouseenter') {
        drawDot({x: x, y: y, hit: hit}, 'red');
    } else if (event.type === 'mouseleave') {
        const r = parseFloat(document.querySelector('[name$=":r-input"]').value);
        refresh(r);
    }
});
