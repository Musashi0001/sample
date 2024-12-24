// BANの種類に応じて期間入力欄を表示/非表示
function toggleDurationInput() {
    const banType = document.getElementById("banType").value;
    const durationField = document.getElementById("durationField");
    const durationInput = document.getElementById("durationDays");

    if (banType === "PERMANENT") {
        durationField.style.display = "none"; // 非表示
        durationInput.removeAttribute("required"); // required属性を削除
    } else {
        durationField.style.display = "block"; // 表示
        durationInput.setAttribute("required", "required"); // required属性を追加
    }
}


// ページ読み込み時に初期化
document.addEventListener("DOMContentLoaded", () => {
	toggleDurationInput(); // 初期状態を設定
	document.getElementById("banType").addEventListener("change", toggleDurationInput);
});