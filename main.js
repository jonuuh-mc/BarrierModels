const edit_canvas = document.getElementById("editingCanvas");
const edit_ctx = edit_canvas.getContext("2d");

// const view_canvas = document.getElementById("viewingCanvas");
// const view_ctx = view_canvas.getContext("2d");

const imgInput = document.getElementById("imgInput");
const x_slider = document.getElementById("px_x")
const y_slider = document.getElementById("px_y")


let currentImgEdge;
let initialPixelData;
let replacementPixelData;
let replacementPixelX = 7
let replacementPixelY = 7

x_slider.addEventListener("mousemove", () => {
    let val = x_slider.value
    document.getElementById("x_label").innerHTML = val
    replacementPixelX = val;

    replacementPixelData = edit_ctx.getImageData(replacementPixelX, replacementPixelY, 1, 1);
});

y_slider.addEventListener("mousemove", () => {
    let val = y_slider.value
    document.getElementById("y_label").innerHTML = val
    replacementPixelY = val;

    replacementPixelData = edit_ctx.getImageData(replacementPixelX, replacementPixelY, 1, 1);
});


imgInput.addEventListener("change", () => {
    const img = new Image();
    img.src = URL.createObjectURL(imgInput.files[0]);

    img.onload = () => {
        if (img.width % 16 == 0 && img.height % 16 == 0) {
            if (img.width == img.height) {

                edit_canvas.width = img.width;
                edit_canvas.height = img.width;

                currentImgEdge = img.width - 1

                x_slider.max = currentImgEdge
                y_slider.max = currentImgEdge

                console.log(replacementPixelX + " " + replacementPixelY)

                edit_ctx.drawImage(img, 0, 0);
                initialPixelData = edit_ctx.getImageData(0, 0, img.width, img.width)
                replacementPixelData = edit_ctx.getImageData(replacementPixelX, replacementPixelY, 1, 1);
                // replacementPixelData = edit_ctx.getImageData(7, 7, 1, 1);
            }
        }
        else {
            window.alert("Images dimensions must be a multiple of 16x16")
        }
    };
});

document.getElementById("downloadBtn").onclick = () => {
    let zip = createZip();

    zip.generateAsync({ type: "blob" }).then(function (content) {
        saveAs(content, "CustomBarrierModels.zip");
    });
};

function resetCanvas() {
    edit_ctx.putImageData(initialPixelData, 0, 0);
}

function getBase64CanvasData() {
    return edit_canvas.toDataURL().split(";base64,")[1];
}

function createPng(folder, clearedSides, clearedCorners) {
    clearSides(clearedSides)

    if (typeof clearedCorners !== 'undefined') {
        clearCorners(clearedCorners)
    }

    folder.file(`barrier-${clearedSides.join("")}.png`, getBase64CanvasData(), { base64: true });
    resetCanvas();
}

function clearSides(sides) {
    for (let side of sides) {
        for (let i = 1; i < currentImgEdge; i++) {
            let coords = getPixelCoordsForSide(side, i)
            edit_ctx.putImageData(replacementPixelData, coords[0], coords[1]);
        }
    }
}

function clearCorners(corners) {
    for (let corner of corners) {
        let coords = getPixelCoordsForCorner(corner)
        edit_ctx.putImageData(replacementPixelData, coords[0], coords[1]);
    }
}

function getPixelCoordsForSide(side, i) {
    switch (side) {
        case "n":
            return [i, 0]
        case "e":
            return [currentImgEdge, i]
        case "s":
            return [i, currentImgEdge]
        case "w":
            return [0, i]
    }
}

function getPixelCoordsForCorner(corner) {
    switch (corner) {
        case "nw":
            return [0, 0]
        case "en":
            return [currentImgEdge, 0]
        case "es":
            return [currentImgEdge, currentImgEdge]
        case "sw":
            return [0, currentImgEdge]
    }
}

function createZip() {
    const zip = new JSZip();
    const folder = zip.folder("assets/minecraft/textures/blocks/barrier");

    const initialData = getBase64CanvasData();

    zip.file("pack.png", initialData, { base64: true });

    let scuffed = "{\n  'pack': {\n    'pack_format': 1,\n    'description': ''\n  }\n}"
    zip.file("pack.mcmeta", scuffed);

    // let sidePowerSet = [
    //     [],

    //     ['E'],
    //     ['N'],
    //     ['S'],
    //     ['W'],

    //     ['E', 'N'],
    //     ['E', 'S'],
    //     ['E', 'W'],
    //     ['N', 'S'],
    //     ['N', 'W'],
    //     ['S', 'W'],

    //     ['E', 'N', 'S'],
    //     ['E', 'N', 'W'],
    //     ['E', 'S', 'W'],
    //     ['N', 'S', 'W'],

    //     ['E', 'N', 'S', 'W']
    // ]

    folder.file("barrier.png", initialData, { base64: true });

    createPng(folder, ['e'])
    createPng(folder, ['n'])
    createPng(folder, ['s'])
    createPng(folder, ['w'])

    createPng(folder, ['e', 'n'], ['en'])
    createPng(folder, ['e', 's'], ['es'])
    createPng(folder, ['e', 'w'])
    createPng(folder, ['n', 's'])
    createPng(folder, ['n', 'w'], ['nw'])
    createPng(folder, ['s', 'w'], ['sw'])

    createPng(folder, ["e", "n", "s"], ["en", "es"])
    createPng(folder, ["e", "n", "w"], ["nw", "en"])

    createPng(folder, ["e", "s", "w"], ["es", "sw"]) // TODO:

    createPng(folder, ["n", "s", "w"], ["nw", "sw"])

    createPng(folder, ["e", "n", "s", "w"], ["nw", "en", "es", "sw"])

    return zip;
}
