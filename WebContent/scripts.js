<script type="text/javascript">
var request = new XMLHttpRequest();

function submitForm(method, url, formId) {
	var form = document.getElementById(formId);
	var mimeType = "application/x-www-form-urlencoded";
	request.open(method, url);
	request.setRequestHeader("Content-Type", mimeType);
	request.send(new FormData(form));
}
</script>