<script type="text/javascript">
var request = new XMLHttpRequest();

function submitForm(method, url, fields) {
	var mimeType = "application/x-www-form-urlencoded";
	request.open(method, url);
	request.setRequestHeader("Content-Type", mimeType);
	request.send(encodeFields(fields));
}

function encodeFields(fields) {
	var values = [];
	for (var field of fields) {
		values.push(encodeURIComponent(field) + '=' + encodeURIComponent(document.getElementById(field).value));
	}
	return values.join('&').replace(/%20/g, '+');
}
</script>
