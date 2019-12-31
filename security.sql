INSERT INTO role_hierarchy (id, child_name, parent_name) VALUES (1, 'ROLE_ADMIN', null)
INSERT INTO public.role_hierarchy (id, child_name, parent_name) VALUES (2, 'ROLE_MANAGER', 'ROLE_ADMIN')
INSERT INTO public.role_hierarchy (id, child_name, parent_name) VALUES (3, 'ROLE_USER', 'ROLE_MANAGER')

INSERT INTO access_ip (id, ip_address) VALUES (1, '127.0.0.1')