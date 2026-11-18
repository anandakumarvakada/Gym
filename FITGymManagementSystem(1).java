import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FITGymManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();
    private static final PaymentService paymentService = new PaymentService();
    private static final MemberService memberService = new MemberService();

    public static void main(String[] args) {
        initializeMembershipPlans();

        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    userLogin();
                    break;
                case "3":
                    adminLogin();
                    break;
                case "4":
                    running = false;
                    System.out.println("\nThank you for using FIT Gym!");
                    break;
                default:
                    System.out.println("\nInvalid choice! Please select 1-4.");
            }
        }
        scanner.close();
    }

    private static void initializeMembershipPlans() {
        // Add your membership-plan initialization here.
    }

    private static void displayMainMenu() {
        System.out.println("\n========== FIT GYM ==========");
        System.out.println("1. Register User");
        System.out.println("2. User Login");
        System.out.println("3. Admin Login");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void registerUser() {
        System.out.print("Enter Full Name: ");
        String fullName = scanner.nextLine();

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();

        LocalDate dateOfBirth;
        while (true) {
            try {
                System.out.print("Enter Date of Birth (yyyy-MM-dd): ");
                String dobInput = scanner.nextLine();
                dateOfBirth = LocalDate.parse(
                    dobInput, DateTimeFormatter.ofPattern("yyyy-MM-dd")
                );
                break;
            } catch (Exception e) {
                System.out.println("Invalid date. Use yyyy-MM-dd.");
            }
        }

        String phone;
        while (true) {
            System.out.print("Enter Phone Number (10 digits): ");
            phone = scanner.nextLine();
            if (phone.matches("[0-9]{10}")) {
                break;
            }
            System.out.println("Please enter exactly 10 digits.");
        }

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        User user = userService.registerUser(
            username, password, fullName, gender, dateOfBirth,
            phone, email, address
        );

        if (user != null) {
            System.out.println("Registration successful. Your User ID: " + user.getUserId());
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    private static void userLogin() {
        System.out.println("User login functionality needs to be connected to your login service.");
    }

    private static void adminLogin() {
        System.out.println("Admin login functionality needs to be connected to your admin service.");
    }

    // Example payment creation method. Call it after calculating the payment details.
    private static void recordPayment(String paymentId, String memberId, String userId,
                                      String membershipPlan, double amount,
                                      String paymentMethod, LocalDate membershipStartDate,
                                      LocalDate membershipEndDate) {
        Payment payment = new Payment(
            paymentId, memberId, userId, membershipPlan, amount,
            paymentMethod, "SUCCESS", LocalDateTime.now(),
            membershipStartDate, membershipEndDate
        );

        boolean saved = paymentService.savePayment(payment);
        System.out.println(saved ? "Payment saved successfully." : "Payment could not be saved.");
    }

    private static void showPaymentHistory() {
        List<Payment> paymentHistory = paymentService.getAllPayments();

        if (paymentHistory.isEmpty()) {
            System.out.println("No payment history found.");
        } else {
            for (int i = 0; i < paymentHistory.size(); i++) {
                System.out.println("Payment #" + (i + 1));
                System.out.println(paymentHistory.get(i));
            }
        }
    }

    private static void checkIn(boolean membershipActive, List<String> attendanceHistory) {
        if (!membershipActive) {
            System.out.println("You need an active membership to check in.");
        } else {
            String checkInDate = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            );
            attendanceHistory.add("Check-In | Date & Time: " + checkInDate);
            System.out.println("CHECK-IN SUCCESSFUL!");
        }
    }

    private static void displayAllMembers() {
        List<Member> members = memberService.getAllMembers();

        if (members.isEmpty()) {
            System.out.println("No members found.");
        } else {
            System.out.println("Total Members : " + members.size());
            for (Member member : members) {
                System.out.println(member);
            }
        }
    }
}

abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
    private String address;

    public Person() {
    }

    public Person(String fullName, String gender, LocalDate dateOfBirth,
                  String phone, String email, String address) {
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

class User extends Person {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String username;
    private String password;
    private String status;

    public User() {
    }

    public User(String userId, String username, String password, String fullName,
                String gender, LocalDate dateOfBirth, String phone, String email,
                String address, String status) {
        super(fullName, gender, dateOfBirth, phone, email, address);
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "User{userId='" + userId + "', username='" + username
            + "', fullName='" + getFullName() + "', phone='" + getPhone()
            + "', status='" + status + "'}";
    }
}

class UserService {
    private final List<User> users = new ArrayList<>();

    public User registerUser(String username, String password, String fullName,
                             String gender, LocalDate dateOfBirth, String phone,
                             String email, String address) {
        User user = new User(
            "U" + (users.size() + 1), username, password, fullName,
            gender, dateOfBirth, phone, email, address, "ACTIVE"
        );
        users.add(user);
        return user;
    }
}

class FileDatabaseManager {
    private static final String DATABASE_FOLDER = "database";

    private static void createDatabaseFolder() {
        File folder = new File(DATABASE_FOLDER);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IllegalStateException("Could not create database folder.");
        }
    }

    public static <T> void save(String fileName, List<T> data) throws IOException {
        createDatabaseFolder();
        File file = new File(DATABASE_FOLDER, fileName);
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(file))) {
            output.writeObject(data);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> load(String fileName)
            throws IOException, ClassNotFoundException {
        createDatabaseFolder();
        File file = new File(DATABASE_FOLDER, fileName);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(file))) {
            return (List<T>) input.readObject();
        }
    }
}

class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    private String paymentId;
    private String memberId;
    private String userId;
    private String membershipPlan;
    private double amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime paymentDate;
    private LocalDate membershipStartDate;
    private LocalDate membershipEndDate;

    public Payment(String paymentId, String memberId, String userId,
                   String membershipPlan, double amount, String paymentMethod,
                   String status, LocalDateTime paymentDate,
                   LocalDate membershipStartDate, LocalDate membershipEndDate) {
        this.paymentId = paymentId;
        this.memberId = memberId;
        this.userId = userId;
        this.membershipPlan = membershipPlan;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paymentDate = paymentDate;
        this.membershipStartDate = membershipStartDate;
        this.membershipEndDate = membershipEndDate;
    }

    @Override
    public String toString() {
        return "Payment{paymentId='" + paymentId + "', memberId='" + memberId
            + "', userId='" + userId + "', plan='" + membershipPlan
            + "', amount=" + amount + ", method='" + paymentMethod
            + "', status='" + status + "', paymentDate=" + paymentDate + "}";
    }
}

class PaymentDAO {
    private static final String FILE_NAME = "payments.dat";

    public List<Payment> findAll() throws IOException, ClassNotFoundException {
        return FileDatabaseManager.load(FILE_NAME);
    }

    public void saveAll(List<Payment> payments) throws IOException {
        FileDatabaseManager.save(FILE_NAME, payments);
    }

    public void addPayment(Payment payment) throws IOException, ClassNotFoundException {
        List<Payment> payments = findAll();
        payments.add(payment);
        saveAll(payments);
    }
}

class PaymentService {
    private final PaymentDAO paymentDAO = new PaymentDAO();

    public boolean savePayment(Payment payment) {
        try {
            paymentDAO.addPayment(payment);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error saving payment: " + e.getMessage());
            return false;
        }
    }

    public List<Payment> getAllPayments() {
        try {
            return paymentDAO.findAll();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading payments: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String memberId;
    private String userId;
    private String fullName;

    public Member(String memberId, String userId, String fullName) {
        this.memberId = memberId;
        this.userId = userId;
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Member{memberId='" + memberId + "', userId='" + userId
            + "', fullName='" + fullName + "'}";
    }
}

class MemberService {
    public List<Member> getAllMembers() {
        // Connect this method to your MemberDAO when that class is available.
        return new ArrayList<>();
    }
}
