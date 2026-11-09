# JobShield AI — Comprehensive Viva Voce Defense Question Bank

This document provides model answers to the most common, rigorous, and technical questions examiners and evaluators ask during final-year engineering project assessments.

---

## Part 1: Architecture & Software Engineering

### Q1: Why did you choose a Modular Monolith in Spring Boot instead of Microservices?
> **Answer:**  
> *"Microservices introduce distributed system complexity, including network serialization overhead, distributed transactions, eventual consistency issues, and service mesh management. For an application with tightly coupled domain workflows—such as authenticating users, analyzing job text, executing heuristic checks, and logging audit events—a modular monolith with clean architectural boundaries (Controllers, Services, Repositories, DTOs) provides sub-millisecond in-memory method invocations, ACID transactional integrity, and simpler deployment while preserving independent modularity."*

### Q2: How does the Spring Boot backend communicate with the Python FastAPI ML microservice?
> **Answer:**  
> *"Communication occurs over internal HTTP REST calls using Spring's `RestClient` or `WebClient`. The connection is secured using an internal API secret passed in the `X-Internal-Secret` header, and the FastAPI service binds strictly to localhost or an internal container network (`jobshield-net`), ensuring external clients cannot query the ML service directly without traversing Spring Security and rate-limiting filters."*

### Q3: How do you handle Cross-Origin Resource Sharing (CORS) and Stateless Authentication?
> **Answer:**  
> *"We use Spring Security 6 configured with stateless session creation (`SessionCreationPolicy.STATELESS`). Authentication is mediated via JSON Web Tokens (JWT) signed with HMAC-SHA256 (`Keys.hmacShaKeyFor`). On each incoming request, our `JwtAuthenticationFilter` extracts the Bearer token, validates its signature and expiration against the server key, parses user roles, and establishes a `UsernamePasswordAuthenticationToken` in the `SecurityContextHolder`. CORS is configured on the backend using `CorsConfigurationSource` to whitelist the web and mobile origins."*

---

## Part 2: Machine Learning, NLP & Explainable AI (XAI)

### Q4: Why did you choose a Hybrid Model (Heuristics + Machine Learning) rather than relying exclusively on an ML classifier?
> **Answer:**  
> *"Machine learning models are probabilistic and can suffer from false negatives when scam syndicates alter vocabulary or use polite phrasing. However, certain actions are definitive indicators of recruitment fraud under employment law—such as requiring a candidate to deposit an upfront registration or laptop fee via UPI. Relying solely on ML creates vulnerability to adversarial evasion. Our hybrid architecture enforces deterministic rules for non-negotiable red flags, while leveraging the calibrated Scikit-learn classifier to detect subtle linguistic patterns like false urgency, vague role scopes, and unrealistic compensation ratios."*

### Q5: How is the Explainable AI (XAI) token attribution computed?
> **Answer:**  
> *"Unlike black-box neural networks, our linear model maintains a direct mathematical mapping between input n-grams and class predictions. Our FastAPI pipeline maps the vectorizer's vocabulary back to the classifier's feature weights. For each token in the input, we compute its directional contribution to the fraud class. Tokens with positive weights (e.g., 'refundable registration fee', 'no interview', 'instant appointment') are tagged with severity scores and character spans, enabling the React frontend to wrap them in color-coded badges with explanatory tooltips."*

### Q6: Why did you use F1-Score and Recall rather than Accuracy to evaluate your model?
> **Answer:**  
> *"Recruitment fraud datasets often exhibit class imbalance. In such scenarios, a naive classifier that simply labels every job as legitimate could achieve 90% accuracy while failing completely at fraud detection. In cyber fraud prevention, Recall is paramount because a False Negative means an applicant is not warned and may suffer financial extortion. Precision is also critical to prevent legitimate businesses from being unfairly penalized. F1-Score, being the harmonic mean of Precision and Recall, provides a balanced measure of the system's true efficacy."*

---

## Part 3: Cybersecurity & Threat Mitigation

### Q7: What is Server-Side Request Forgery (SSRF), and how does your application prevent it?
> **Answer:**  
> *"SSRF occurs when an attacker inputs a URL pointing to internal network infrastructure—such as `http://127.0.0.1:8080/admin`, `http://192.168.1.1`, or cloud metadata services like `http://169.254.169.254/latest/meta-data/`—causing the backend server to make unauthorized internal requests. In JobShield AI, our `SafeUrlScannerService` resolves the host using `InetAddress.getAllByName(domain)` and evaluates every returned IP address. If an IP falls within RFC 1918 private ranges, loopback addresses (`127.0.0.0/8`), or link-local subnets, the connection is aborted before opening a network socket. When following redirects, destination IPs are validated at each hop up to a maximum depth of 3."*

### Q8: How does your Typosquatting and Homoglyph detection engine work?
> **Answer:**  
> *"Attackers frequently register lookalike domains to impersonate legitimate enterprises (e.g., `tcs-careers-interview.in` or `infosys-jobs.biz`). Our engine computes the Levenshtein edit distance between the extracted URL domain and a verified repository of enterprise corporate domains (`tcs.com`, `infosys.com`, `wipro.com`, `google.com`). If the edit distance is within an alert threshold of 1 to 3 edits, or if an unapproved top-level domain mimics a known corporate name, the engine flags it as a suspected typosquatting impersonation."*

### Q9: How does the system handle SQL Injection and Cross-Site Scripting (XSS)?
> **Answer:**  
> *"SQL injection is prevented by utilizing Spring Data JPA and Hibernate, which compile queries into parameterized prepared statements (`SELECT c FROM Company c WHERE LOWER(c.name) LIKE ...`), ensuring user inputs are treated as literal values rather than executable code. XSS is mitigated in React and our HTML client through contextual output encoding, escaping user input before rendering, and isolating raw HTML through verified sanitized components."*

---

## Part 4: Database Design & Scalability

### Q10: Why did you use a combination of relational columns and JSON columns in MySQL?
> **Answer:**  
> *"Core entities requiring relational integrity, indexing, foreign keys, and referential actions—such as `users`, `companies`, `job_analyses`, `scam_reports`, and `audit_logs`—are stored in normalized relational columns. However, unstructured, dynamically sized data—such as extracted metadata maps, audit details, and XAI token attribution spans—are stored in MySQL 8's native JSON columns. This provides flexibility for evolving NLP models without requiring schema migrations on every release."*

### Q11: How is user privacy safeguarded in community scam reports?
> **Answer:**  
> *"When a user submits a scam report, their identity is stored internally for administrative audit trails, but public community feeds display reports anonymously without exposing the reporter's personal details. Furthermore, submitted evidence is reviewed by an administrator before reports are marked as verified, preventing defamatory or unverified allegations from appearing publicly as confirmed fraud."*

---

## Part 5: Societal Impact & UN Sustainable Development Goals

### Q12: How does your engineering project align with UN SDG 8 (Decent Work and Economic Growth)?
> **Answer:**  
> *"Target 8.8 emphasizes protecting labor rights and promoting safe working environments for all workers. Recruitment scams extort money from unemployed youth, forcing many into debt and eroding trust in digital hiring platforms. By providing an automated verification engine that warns candidates against paying illegal registration fees and directs them to verified resources like the National Career Service (NCS) and the 1930 Cybercrime Helpline, JobShield AI fosters a transparent and safe recruitment ecosystem."*

