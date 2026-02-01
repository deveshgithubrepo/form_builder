alter table forms
add column payment_needed boolean default false after approval_needed;

update forms set payment_needed = false where payment_needed is null;