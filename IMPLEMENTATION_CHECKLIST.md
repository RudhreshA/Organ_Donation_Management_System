# Implementation Plan Checklist (REPLANNED)

## Original Question/Task

**Question:** <h1>Organ Donation Management System</h1>

<h2>Overview</h2>
<p>You are tasked with developing a Organ Donation Management System that helps connect Organ donors with hospitals and Organ banks. The system will allow donors to register, hospitals to request Organ, and track Organ availability across different locations.</p>

<h2>Question Requirements</h2>

<h3>1. Backend Requirements (Spring Boot)</h3>

<h4>1.1 Donor Management</h4>
<p>Create a REST API to manage Organ donors with the following endpoints:</p>
<ul>
    <li><b>Register a new donor</b>: POST /api/donors
        <ul>
            <li>Request body should include: name, age, gender, OrganType, contactNumber, email, address, and lastDonationDate (optional)</li>
            <li>Validate that the donor is at least 18 years old</li>
            <li>Validate that the Organ type is one of: A+, A-, B+, B-, AB+, AB-, O+, O-</li>
            <li>Return status code 201 with the created donor object on success</li>
            <li>Return status code 400 with appropriate error message for validation failures</li>
        </ul>
    </li>
    <li><b>Get all donors</b>: GET /api/donors
        <ul>
            <li>Return a list of all registered donors</li>
            <li>Return status code 200 with an empty list if no donors exist</li>
        </ul>
    </li>
    <li><b>Get donor by ID</b>: GET /api/donors/{id}
        <ul>
            <li>Return the donor with the specified ID</li>
            <li>Return status code 404 if donor with given ID does not exist</li>
        </ul>
    </li>
</ul>

<h4>Example Donor JSON:</h4>
<code>
{
  "id": 1,
  "name": "John Doe",
  "age": 25,
  "gender": "Male",
  "OrganType": "O+",
  "contactNumber": "1234567890",
  "email": "john@example.com",
  "address": "123 Main St, City",
  "lastDonationDate": "2023-01-15"
}
</code>

<h4>1.2 Organ Request Management</h4>
<p>Create a REST API to manage Organ requests with the following endpoints:</p>
<ul>
    <li><b>Create a Organ request</b>: POST /api/requests
        <ul>
            <li>Request body should include: hospitalName, OrganType, unitsRequired, urgency (HIGH/MEDIUM/LOW), patientName, contactPerson, contactNumber, and requestDate</li>
            <li>Validate that unitsRequired is a positive number</li>
            <li>Validate that the Organ type is valid</li>
            <li>Return status code 201 with the created request object on success</li>
            <li>Return status code 400 with appropriate error message for validation failures</li>
        </ul>
    </li>
    <li><b>Get all Organ requests</b>: GET /api/requests
        <ul>
            <li>Return a list of all Organ requests</li>
            <li>Return status code 200 with an empty list if no requests exist</li>
        </ul>
    </li>
    <li><b>Update request status</b>: PUT /api/requests/{id}
        <ul>
            <li>Request body should include: status (PENDING, FULFILLED, CANCELLED)</li>
            <li>Return status code 200 with the updated request object on success</li>
            <li>Return status code 404 if request with given ID does not exist</li>
        </ul>
    </li>
</ul>

<h4>Example Organ Request JSON:</h4>
<code>
{
  "id": 1,
  "hospitalName": "City General Hospital",
  "OrganType": "A+",
  "unitsRequired": 3,
  "urgency": "HIGH",
  "patientName": "Jane Smith",
  "contactPerson": "Dr. Robert Johnson",
  "contactNumber": "9876543210",
  "requestDate": "2023-06-10",
  "status": "PENDING"
}
</code>

<h3>2. Frontend Requirements (React)</h3>

<h4>2.1 Donor Registration Form</h4>
<p>Create a form component to register new donors with the following features:</p>
<ul>
    <li>Input fields for all donor properties (name, age, gender, OrganType, contactNumber, email, address, lastDonationDate)</li>
    <li>Form validation for all fields:
        <ul>
            <li>Name: Required, minimum 3 characters</li>
            <li>Age: Required, must be at least 18</li>
            <li>Gender: Required, select from options (Male, Female, Other)</li>
            <li>Organ Type: Required, select from valid Organ types</li>
            <li>Contact Number: Required, valid phone number format</li>
            <li>Email: Required, valid email format</li>
            <li>Address: Required</li>
            <li>Last Donation Date: Optional, must be a valid date not in the future</li>
        </ul>
    </li>
    <li>Display appropriate error messages for validation failures</li>
    <li>Submit button that is disabled until all required fields are valid</li>
    <li>Success message upon successful submission</li>
</ul>

<h4>2.2 Organ Request Dashboard</h4>
<p>Create a dashboard component to display Organ requests with the following features:</p>
<ul>
    <li>Table or card-based view of all Organ requests</li>
    <li>Each request should display:
        <ul>
            <li>Hospital name</li>
            <li>Organ type requested</li>
            <li>Units required</li>
            <li>Urgency level (with visual indicator - red for HIGH, yellow for MEDIUM, green for LOW)</li>
            <li>Request date</li>
            <li>Status (PENDING, FULFILLED, CANCELLED)</li>
        </ul>
    </li>
    <li>Filter options:
        <ul>
            <li>Filter by Organ type</li>
            <li>Filter by status</li>
        </ul>
    </li>
    <li>Sort options:
        <ul>
            <li>Sort by request date (newest/oldest)</li>
            <li>Sort by urgency level</li>
        </ul>
    </li>
</ul>

<h4>2.3 Organ Availability Component</h4>
<p>Create a component to display Organ availability with the following features:</p>
<ul>
    <li>Visual representation (e.g., progress bars, charts) showing available units for each Organ type</li>
    <li>Color-coded indicators for Organ types with low availability (less than 5 units)</li>
    <li>Display the last updated timestamp</li>
    <li>Refresh button to update the availability data</li>
</ul>

<h3>3. Data Models</h3>

<h4>3.1 Donor Entity (Backend)</h4>
<p>Create a Donor entity with the following attributes:</p>
<ul>
    <li>id (Long): Primary key</li>
    <li>name (String): Donor's full name</li>
    <li>age (Integer): Donor's age</li>
    <li>gender (String): Donor's gender</li>
    <li>OrganType (String): Donor's Organ type</li>
    <li>contactNumber (String): Donor's contact number</li>
    <li>email (String): Donor's email address</li>
    <li>address (String): Donor's address</li>
    <li>lastDonationDate (LocalDate): Date of last donation (nullable)</li>
</ul>

<h4>3.2 OrganRequest Entity (Backend)</h4>
<p>Create a OrganRequest entity with the following attributes:</p>
<ul>
    <li>id (Long): Primary key</li>
    <li>hospitalName (String): Name of the requesting hospital</li>
    <li>OrganType (String): Required Organ type</li>
    <li>unitsRequired (Integer): Number of units required</li>
    <li>urgency (Enum): Urgency level (HIGH, MEDIUM, LOW)</li>
    <li>patientName (String): Name of the patient</li>
    <li>contactPerson (String): Name of the contact person at the hospital</li>
    <li>contactNumber (String): Contact number for the request</li>
    <li>requestDate (LocalDate): Date of the request</li>
    <li>status (Enum): Status of the request (PENDING, FULFILLED, CANCELLED)</li>
</ul>

<h3>4. Integration Requirements</h3>

<p>Ensure that the frontend components properly integrate with the backend APIs:</p>
<ul>
    <li>The donor registration form should submit data to the POST /api/donors endpoint</li>
    <li>The Organ request dashboard should fetch data from the GET /api/requests endpoint</li>
    <li>The Organ availability component should fetch data from an appropriate endpoint (you may need to create this)</li>
    <li>Implement proper error handling for API calls, displaying user-friendly error messages</li>
    <li>Implement loading indicators during API calls</li>
</ul>

<p>Note: This application uses MySQL as the backend database.</p>

**Created:** 2025-07-26 16:53:18 (Replan #2)
**Total Steps:** 4
**Previous Execution:** 3 steps completed before replanning

## Replanning Context
- **Replanning Attempt:** #2
- **Trigger:** V2 execution error encountered

## Previously Completed Steps

✅ Step 1: Implement ALL Jest frontend test cases - PART 1 (DonorRegistrationForm and DonorFormValidation)
✅ Step 2: Implement ALL Jest frontend test cases - PART 2 (OrganRequestDashboard)
✅ Step 3: Implement ALL Jest frontend test cases - PART 3 (OrganAvailabilityComponent and ErrorHandling)

## NEW Implementation Plan Checklist

### Step 1: FIX DonorRegistrationForm re-render infinite loop issue (FAILED JEST TESTS STEP)
- [x] **Status:** ✅ Completed
- **Files to modify:**
  - /home/coder/project/workspace/question_generation_service/solutions/332a0778-9402-4e30-ae6a-c44dd4be37ff/reactapp/src/components/DonorRegistrationForm.js
  - /home/coder/project/workspace/question_generation_service/solutions/332a0778-9402-4e30-ae6a-c44dd4be37ff/reactapp/src/components/DonorRegistrationForm.test.js
- **Description:** This step addresses the root cause of the Jest suite failure: 'Too many re-renders'. The code will be fixed so that validation or helper code does not trigger state updates during React renders. All setState-like side effects must be constrained to event handlers or effects, not within synchronous render/validation flows. After code changes, confirm test runs pass locally for the form component.

### Step 2: Implement ALL Jest frontend test cases - PART 2 (OrganRequestDashboard)
- [x] **Status:** ✅ Completed
- **Files to modify:**
  - /home/coder/project/workspace/question_generation_service/solutions/332a0778-9402-4e30-ae6a-c44dd4be37ff/reactapp/src/components/OrganRequestDashboard.test.js
- **Description:** Continuing Jest frontend test implementation; this part covers dashboard tests including mock data, UI checks for urgency/status, filters, and sort functionality.

### Step 3: Implement ALL Jest frontend test cases - PART 3 (OrganAvailabilityComponent and ErrorHandling)
- [x] **Status:** ✅ Completed
- **Files to modify:**
  - /home/coder/project/workspace/question_generation_service/solutions/332a0778-9402-4e30-ae6a-c44dd4be37ff/reactapp/src/components/OrganAvailabilityComponent.test.js
  - /home/coder/project/workspace/question_generation_service/solutions/332a0778-9402-4e30-ae6a-c44dd4be37ff/reactapp/src/components/DonorRegistrationForm.test.js
- **Description:** Final part for frontend Jest tests: covers Organ availability analytics, refresh, status colors, and general error handling in forms. Includes investigation of proper teardown, state restoration, and error display as per specs.

### Step 4: Compile and test frontend (React/Jest)
- [ ] **Status:** ⏳ Not Started
- **Description:** Validates that all React UI compiles, builds, lints, and passes required automated test cases, confirming integration and correctness.

## NEW Plan Completion Status

| Step | Status | Completion Time |
|------|--------|----------------|
| Step 1 | ✅ Completed | 2025-07-26 16:54:18 |
| Step 2 | ✅ Completed | 2025-07-26 16:55:05 |
| Step 3 | ✅ Completed | 2025-07-26 16:55:18 |
| Step 4 | ⏳ Not Started | - |

## Notes & Issues

### Replanning History
- Replan #2: V2 execution error encountered

### Errors Encountered
- None yet in new plan

### Important Decisions
- Step 3: OrganAvailabilityComponent.test.js and DonorRegistrationForm.test.js fully implement all error, display, reload, and low availability UI tests according to requirements. The main components already implement proper error and edge handling and work with the mocks as defined.

### Next Actions
- Resume implementation following the NEW checklist
- Use `update_plan_checklist_tool` to mark steps as completed
- Use `read_plan_checklist_tool` to check current status

---
*This checklist was updated due to replanning. Previous progress is preserved above.*