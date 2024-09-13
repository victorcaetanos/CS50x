SELECT title FROM people p
JOIN stars s ON (p.id = s.person_id AND m.id = s.movie_id)
JOIN movies m ON (m.id = s.movie_id)
JOIN ratings r ON (m.id = r.movie_id)
WHERE name = 'Chadwick Boseman'
ORDER BY rating DESC
LIMIT 5;
