const allCheckBox = document.querySelector('.all-check-box');
const typeCheckBoxes = document.querySelectorAll('.type-check-box');

// Initialise
allCheckBox.checked = true;
typeCheckBoxes.forEach( checkbox => checkbox.checked = true);

allCheckBox.addEventListener('change', function() {
	const checkedStatus = this.checked;
	// update all other checkboxes and call handle functions
	typeCheckBoxes.forEach(typeBox => {
		typeBox.checked = checkedStatus;
		handleCheckboxChange(typeBox);
	});
})

typeCheckBoxes.forEach( checkbox => checkbox.addEventListener('change', function() {
	handleCheckboxChange(this);
	updateAllCheckBox();
}));


function handleCheckboxChange(checkbox) {
	(checkbox.checked) ? showItemsOfType(checkbox.id) : hideItemsOfType(checkbox.id);
}

function showItemsOfType(typeId) {
	const items = document.querySelectorAll(`[data-type-id='${typeId}']`);
	items.forEach(item => item.style.transform = 'scaleY(1)');
}

function hideItemsOfType(typeId) {
	const items = document.querySelectorAll(`[data-type-id='${typeId}']`);
	items.forEach(item => item.style.transform = 'scaleY(0)');
}

function updateAllCheckBox() {
	let typesArray = Array.from(typeCheckBoxes);
	const allChecked = typesArray.every(type => type.checked);
	const noneChecked = typesArray.every(type => !type.checked);
	if (allChecked) {
		allCheckBox.checked = true;
		allCheckBox.indeterminate = false;
	}
	if (noneChecked) {
		allCheckBox.checked = false;
		allCheckBox.indeterminate = false;
	}
	if (!allChecked && !noneChecked) {
		allCheckBox.checked = false;
		allCheckBox.indeterminate = true;
	}
}
