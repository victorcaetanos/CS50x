SELECT s.name FROM songs s INNER JOIN artists a ON (s.artist_id = a.id) WHERE a.name like "Post Malone";
