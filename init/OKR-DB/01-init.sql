



INSERT INTO
  tbl_Role (id, name)
VALUES
  (1, 'CO_OKR_Admin'),
  (2, 'BUO_OKR_Admin'),
  (3, 'READ_Only_User');


INSERT INTO
  tbl_Privilege (id, name)
VALUES
  (1, 'CO_READ'),
  (2, 'CO_WRITE'),
  (3, 'BUO_READ'),
  (4, 'BUO_WRITE_OWN');


INSERT INTO
  tbl_Role_includes_Privilege (role_id, privilege_id)
VALUES
  (1, 2),
  (2, 4),
  (3, 1),
  (3, 3);


INSERT INTO
  tbl_Role_inherits_Role (role_id, inherit_id)
VALUES
  (1, 3),
  (2, 3);




INSERT INTO
  tbl_KeyResultType (name)
VALUES
  ('numeric'),
  ('percentual'),
  ('binary');

