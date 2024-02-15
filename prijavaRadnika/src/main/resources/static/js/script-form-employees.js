document.addEventListener("DOMContentLoaded", function() {
    var employmentContractControl = document.getElementById("employmentContract");
    var reasonForDefiniteControl = document.getElementById("reasonForDefinite");
    var dateOfSignOutControl = document.getElementById("dateOfSignOut");
    var foreignNationalCheckbox = document.getElementById('foreignNational');
    var expiryDateOfWorkPermitInput = document.getElementById('expiryDateOfWorkPermit');
    var workingHoursControl = document.getElementById("workingHours");
    var hoursForPartTimeControl = document.getElementById("hoursForPartTime");
    var additionalWorkCheckbox = document.getElementById("additionalWork");
    var additionalWorkHoursControl = document.getElementById("additionalWorkHours");

    function toggleReasonForDefinite() {
        if (employmentContractControl.value === "Određeno") {
            reasonForDefiniteControl.disabled = false;
            dateOfSignOutControl.disabled = false;
        } else {
            reasonForDefiniteControl.disabled = true;
            dateOfSignOutControl.disabled = true;
        }
    }

    function toggleExpiryDateOfWorkPermitInput() {
        if (foreignNationalCheckbox.checked) {
            expiryDateOfWorkPermitInput.disabled = false;
        } else {
            expiryDateOfWorkPermitInput.disabled = true;
            expiryDateOfWorkPermitInput.value = "";
        }
    }

    function toggleReasonForHoursForPartTime() {
        if (workingHoursControl.value === "Nepuno") {
            hoursForPartTimeControl.disabled = false;
        } else {
            hoursForPartTimeControl.disabled = true;
        }
    }

    function toggleAdditionalWorkInput() {
            if (additionalWorkCheckbox.checked) {
                additionalWorkHoursControl.disabled = false;
            } else {
                additionalWorkHoursControl.disabled = true;
                additionalWorkHoursControl.value = "";
            }
        }

    toggleReasonForDefinite();
    toggleExpiryDateOfWorkPermitInput();
    toggleReasonForHoursForPartTime();
    toggleAdditionalWorkInput();

    employmentContractControl.addEventListener("change", toggleReasonForDefinite);
    foreignNationalCheckbox.addEventListener('change', toggleExpiryDateOfWorkPermitInput);
    workingHoursControl.addEventListener("change", toggleReasonForHoursForPartTime);
    additionalWorkCheckbox.addEventListener('change', toggleAdditionalWorkInput);
});