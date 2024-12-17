// フォームの切り替えスクリプト
document.addEventListener("DOMContentLoaded", () => {
	const loginForm = document.getElementById("login-form");
	const registerForm = document.getElementById("register-form");
	const toggleButton = document.getElementById("toggle-button");
	const formTitle = document.getElementById("form-title");

	// ボタンクリック時の動作
	toggleButton.addEventListener("click", () => {
		// フォームの表示を切り替える
		loginForm.classList.toggle("hidden");
		registerForm.classList.toggle("hidden");

		// タイトルとボタンのテキストを変更
		if (loginForm.classList.contains("hidden")) {
			formTitle.textContent = "新規登録";
			toggleButton.textContent = "ログインに切り替え";
		} else {
			formTitle.textContent = "ログイン";
			toggleButton.textContent = "新規登録に切り替え";
		}
	});
});
