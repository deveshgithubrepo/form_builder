alter table forms
add column form_type varchar(255) after tenant_id;

update forms set form_type = 'REGISTRATION_FORM' where form_type is null;