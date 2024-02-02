




INSERT INTO
  tbl_Company (id, name)
VALUES
  (1, 'Firma XY');

INSERT INTO
  tbl_BuisinessUnit (id, name, company_id)
VALUES
  (1, 'Vertrieb', 1),
  (2, 'Marketing', 1), 
  (3, 'Personal', 1), 
  (4, 'Technologie', 1); 

INSERT INTO
  tbl_Unit (id, name, buisinessunit_id)
VALUES
  (1, 'Frontend Entwicklung', 4), 
  (2, 'Backend Entwicklung', 4), 
  (3, 'Testing', 4), 
  (4, 'Design', 2);





INSERT INTO
  tbl_User (id, username, password, surname, firstname)
VALUES
  (1, 'test', 'test', 'test', 'test');







INSERT INTO
  tbl_Objective (id, deadline, title, description, user_id)
VALUES
  (1, '2024-01-01 00:00:00', 'O1', 'test CO', 1),
  (2, '2024-01-01 00:00:00', 'O2', 'test BUO', 1),
  (3, '2024-01-01 00:00:00', 'O3', 'test BUO', 1);

INSERT INTO
  tbl_CompanyObjective (objective_id, company_id)
VALUES
  (1, 1);

INSERT INTO
  tbl_BuisinessUnitObjective (objective_id, buisinessunit_id)
VALUES
  (2, 1),
  (3, 1);





INSERT INTO
  tbl_keyresult (id, goal, type, title, description, current, confidencelevel, objective_id)
VALUES
  (1, 5, 'numeric', 'K1', 'test KeyResult', 0, 50, 1),
  (2, 100, 'percentual', 'K2', 'test KeyResult', 0, 50, 2),
  (3, 1, 'binary', 'K3', 'test KeyResult', 0, 50, 2);

INSERT INTO
  tbl_companyKeyresult (keyresult_id)
VALUES
  (1);

INSERT INTO
  tbl_BuisinessUnitKeyresult (keyresult_id)
VALUES
  (2),
  (3);















