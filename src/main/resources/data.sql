insert into app_user (id, email, display_name, department) values
                                                               ('759be72d-564f-4384-8a14-0260b25a93d5', 'alice@example.gov', 'Alice Example', 'BMVI'),
                                                               ('bc03b902-726d-4be1-91b8-6a6de2f0ec8b', 'bob@example.gov', 'Bob Example', 'BMF');


insert into service_offering (id, name, description) values
                                                                ('43c139d7-121b-4ea4-85c7-118f03b2249c',  'VPN-Zugang', 'Zugang zum Bundes-VPN'),
                                                                ('42dc9e28-6f6c-43db-9826-7a663402c9ae', 'E-Akte Rolle', 'Rollenbeantragung für E-Akte');

insert into ticket
(id, subject, description, status, priority,
 requester_id, assignee_id, service_offering_id,
 created_at, updated_at)
values
    (
        random_uuid(),
        'VPN-Zugang beantragen',
        'Bitte VPN für Alice freischalten.',
        'OPEN',
        'HIGH',
        '759be72d-564f-4384-8a14-0260b25a93d5',   -- Alice
        'bc03b902-726d-4be1-91b8-6a6de2f0ec8b',   -- Bob
        '43c139d7-121b-4ea4-85c7-118f03b2249c',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );