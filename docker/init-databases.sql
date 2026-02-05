-- Initialize all databases for fullstack Java projects

-- Job Portal Database
CREATE DATABASE job_portal;

-- E-Commerce Database
CREATE DATABASE ecommerce;

-- Food Ordering Database
CREATE DATABASE food_ordering;

-- Event Booking Database
CREATE DATABASE event_booking;

-- Grant privileges (in case we need a separate user later)
GRANT ALL PRIVILEGES ON DATABASE job_portal TO postgres;
GRANT ALL PRIVILEGES ON DATABASE ecommerce TO postgres;
GRANT ALL PRIVILEGES ON DATABASE food_ordering TO postgres;
GRANT ALL PRIVILEGES ON DATABASE event_booking TO postgres;
