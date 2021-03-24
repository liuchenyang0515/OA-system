DELETE FROM `adm_leave_form`;
INSERT INTO `adm_leave_form`(`form_id`, `employee_id`, `form_type`, `start_time`, `end_time`, `reason`, `create_time`, `state`) VALUES (31, 4, 3, '2020-03-29 00:00:00', '2020-04-04 00:00:00', '事故,小腿骨折,明天做手术', '2020-03-28 11:49:09', 'processing');
INSERT INTO `adm_leave_form`(`form_id`, `employee_id`, `form_type`, `start_time`, `end_time`, `reason`, `create_time`, `state`) VALUES (32, 3, 1, '2020-03-29 00:00:00', '2020-04-04 00:00:00', '没啥原因,单纯想休息几天', '2020-03-28 11:50:35', 'processing');
INSERT INTO `adm_leave_form`(`form_id`, `employee_id`, `form_type`, `start_time`, `end_time`, `reason`, `create_time`, `state`) VALUES (33, 6, 1, '2020-03-30 00:00:00', '2020-03-31 00:00:00', '带孩子去看牙', '2020-03-28 11:51:49', 'processing');


DELETE FROM `adm_process_flow`;
INSERT INTO `adm_process_flow`(`process_id`, `form_id`, `operator_id`, `action`, `result`, `reason`, `create_time`, `audit_time`, `order_no`, `state`, `is_last`) VALUES (74, 31, 4, 'apply', NULL, NULL, '2020-03-28 11:49:09', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow`(`process_id`, `form_id`, `operator_id`, `action`, `result`, `reason`, `create_time`, `audit_time`, `order_no`, `state`, `is_last`) VALUES (75, 31, 2, 'audit', NULL, NULL, '2020-03-28 11:49:09', NULL, 2, 'process', 0);
INSERT INTO `adm_process_flow`(`process_id`, `form_id`, `operator_id`, `action`, `result`, `reason`, `create_time`, `audit_time`, `order_no`, `state`, `is_last`) VALUES (76, 31, 1, 'audit', NULL, NULL, '2020-03-28 11:49:09', NULL, 3, 'ready', 1);
INSERT INTO `adm_process_flow`(`process_id`, `form_id`, `operator_id`, `action`, `result`, `reason`, `create_time`, `audit_time`, `order_no`, `state`, `is_last`) VALUES (77, 32, 3, 'apply', NULL, NULL, '2020-03-28 11:50:35', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow`(`process_id`, `form_id`, `operator_id`, `action`, `result`, `reason`, `create_time`, `audit_time`, `order_no`, `state`, `is_last`) VALUES (78, 32, 2, 'audit', NULL, NULL, '2020-03-28 11:50:35', NULL, 2, 'process', 0);
INSERT INTO `adm_process_flow`(`process_id`, `form_id`, `operator_id`, `action`, `result`, `reason`, `create_time`, `audit_time`, `order_no`, `state`, `is_last`) VALUES (79, 32, 1, 'audit', NULL, NULL, '2020-03-28 11:50:36', NULL, 3, 'ready', 1);
INSERT INTO `adm_process_flow`(`process_id`, `form_id`, `operator_id`, `action`, `result`, `reason`, `create_time`, `audit_time`, `order_no`, `state`, `is_last`) VALUES (80, 33, 6, 'apply', NULL, NULL, '2020-03-28 11:51:49', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow`(`process_id`, `form_id`, `operator_id`, `action`, `result`, `reason`, `create_time`, `audit_time`, `order_no`, `state`, `is_last`) VALUES (81, 33, 1, 'audit', NULL, NULL, '2020-03-28 11:51:49', NULL, 2, 'process', 1);
