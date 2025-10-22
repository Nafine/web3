function handleR() {
    let radius = parseFloat($('#r-input').val());
    refresh(radius);
}

$('#requestTable tbody').on('click mouseenter mouseleave', 'tr', function (event) {
    let row = $(this).children();

    let x = parseFloat(row.eq(0).text());
    let y = parseFloat(row.eq(1).text());
    let hit = row.eq(3).text() === "true";

    if (event.type === 'mouseenter') {
        drawDot({x: x, y: y, hit: hit});
    } else if (event.type === 'mouseleave') {
        refresh(parseFloat($('#r-input').val()));
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

    const xInput = document.querySelector('[name$=":x-hidden"]');
    const xDisplay = document.querySelector('[id$=":x-display"]');

    if (xInput) xInput.value = pos.x;
    if (xDisplay) xDisplay.textContent = 'X: ' + pos.x;


    const yInput = document.querySelector('[name$=":y-input"]');
    if (yInput) yInput.value = pos.y;

    document.querySelector('[id$=":form-submit"]').click();
})