ALTER table forms
add column approval_needed tinyint(1) after cohort,
add column form_status varchar(50) after approval_needed;


ALTER table form_responses
add column form_response_status varchar(50) after response,
add column reject_reason text after form_response_status;