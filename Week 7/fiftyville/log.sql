-- Keep a log of any SQL queries you execute as you solve the mystery.

-- ran this command to find information about the reported crime
SELECT *
FROM crime_scene_reports
where day = 28
and month = 7
and year = 2023
and street = 'Humphrey Street';
-- found that it happened at 10:15am and the interviews containing 'bakery' are the ones I'm looking for


-- ran this command to find information about the interviews
SELECT *
FROM interviews
where day = 28
and month = 7
and year = 2023
and transcript like '%bakery%';
-- found out that the thief used an atm on Leggett Street to take out some money


-- ran this command to find information about the atm used and the names of people related to the uses
SELECT p.id, name, amount
FROM atm_transactions a
join bank_accounts b on (a.account_number = b.account_number)
join people p on (p.id = b.person_id)
where day = 28
and month = 7
and year = 2023
and atm_location = 'Leggett Street'
and transaction_type = 'withdraw'
order by p.id;
-- found 8 possible suspects


-- ran this command to find information about the call that lasted less then a minute
SELECT *
FROM airports
where city = 'Fiftyville';
--found the id of the airport (id 8)


--looking for the flights out of town the next day, ordered by time
SELECT *
FROM airports a
join flights f on (a.id = f.destination_airport_id)
where day = 29
and month = 7
and year = 2023
and origin_airport_id = 8
order by hour, minute;
--found that the earliest flight was to New York City


--looking for suspects in the flight (id 36)
SELECT pe.id, pe.name, pe.passport_number
FROM people pe
join passengers pa on (pe.passport_number = pa.passport_number)
join flights f on (f.id = pa.flight_id)
where day = 29
and month = 7
and year = 2023
and hour = 8
and minute = 20
and origin_airport_id = 8
order by pe.id;
--found 8 suspects
--checking between both lists of suspect, we can see 3 names repeating (kenny, luca and bruce)


--Looking for calls that lasted less the a minute
SELECT pe.id, pe.name, ph.duration, ph.receiver
FROM people pe
join phone_calls ph on (pe.phone_number = ph.caller)
where day = 28
and month = 7
and year = 2023
and duration < 60
order by pe.id;
-- found 9 records, but only 2 fit the previour patterns (kenny and bruce)

--Looking for the receiver of kenny's and bruce's call
SELECT pe.name
FROM people pe
where phone_number in ('(066) 555-9701', '(375) 555-8161');
-- found Doris and Robins respectively (possible accomplices)

-- looking for people who drove away from the bakery
SELECT p.name
FROM people p
join bakery_security_logs b on (p.license_plate = b.license_plate)
where day = 28
and month = 7
and year = 2023
and b.hour = 10
and b.minute BETWEEN 15 and 25
order by p.id;
-- found the names of Luca and Bruce, but since 'Bruce' has been the one in all the other lists, he is the thief, consequently 'Robin' is his accomplice and He went to 'New York City'
