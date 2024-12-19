INSERT INTO "jobs" ("id", "job_type", "plan_id", "job_id", "route_id", "request", "response", "status", "attempts", "action_user", "attempt_time", "request_time", "respond_time", "create_time")
VALUES (1, 'delivery', 101, 'J123', 'R456', 'Sample request data for job 1', 'Sample response data for job 1', 'submitted', 0, 1001, '2024-12-18 10:00:00', '2024-12-18 09:30:00', '2024-12-18 09:45:00', '2024-12-18 09:00:00');

INSERT INTO "jobs" ("id", "job_type", "plan_id", "job_id", "route_id", "request", "response", "status", "attempts", "action_user", "attempt_time", "request_time", "respond_time", "create_time")
VALUES (2, 'pickup', 102, 'J124', 'R457', 'Sample request data for job 2', 'Sample response data for job 2', 'in_progress', 1, 1002, '2024-12-18 11:00:00', '2024-12-18 10:30:00', NULL, '2024-12-18 10:00:00');

INSERT INTO "jobs" ("id", "job_type", "plan_id", "job_id", "route_id", "request", "response", "status", "attempts", "action_user", "attempt_time", "request_time", "respond_time", "create_time")
VALUES (3, 'delivery', 103, 'J125', 'R458', 'Sample request data for job 3', 'Sample response data for job 3', 'optimized', 2, 1003, '2024-12-18 12:00:00', '2024-12-18 11:30:00', '2024-12-18 11:45:00', '2024-12-18 11:00:00');

INSERT INTO "jobs" ("id", "job_type", "plan_id", "job_id", "route_id", "request", "response", "status", "attempts", "action_user", "attempt_time", "request_time", "respond_time", "create_time")
VALUES (4, 'pickup', 104, 'J126', 'R459', 'Sample request data for job 4', 'Sample response data for job 4', 'failed', 3, 1004, '2024-12-18 13:00:00', '2024-12-18 12:30:00', '2024-12-18 12:45:00', '2024-12-18 12:00:00');

INSERT INTO "jobs" ("id", "job_type", "plan_id", "job_id", "route_id", "request", "response", "status", "attempts", "action_user", "attempt_time", "request_time", "respond_time", "create_time")
VALUES (5, 'delivery', 105, 'J127', 'R460', 'Sample request data for job 5', 'Sample response data for job 5', 'submitted', 0, 1005, NULL, '2024-12-18 13:30:00', NULL, '2024-12-18 13:00:00');
