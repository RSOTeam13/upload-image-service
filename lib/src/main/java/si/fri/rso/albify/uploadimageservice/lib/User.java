package si.fri.rso.albify.uploadimageservice.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User {
    private String id;
    private String firstName;
    private String lastName;

    public static ArrayList<User> users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private static void initUsers() { // TODO: figure out how we wanna do users
        User.users = new ArrayList<>();

        User user1 = new User();
            user1.setFirstName("Josip");
            user1.setLastName("Tester");
            user1.setId("123123"); // TODO: hardcode UUID

        User.users.add(user1);
            User user2 = new User();
            user2.setFirstName("LeeRoy");
            user2.setLastName("Travis");
            user2.setId("321321"); // TODO: hardcode UUID
        User.users.add(user2);

        User user3 = new User();
            user3.setFirstName("Git");
            user3.setLastName("Gud");
            user3.setId("420420"); // TODO: hardcode UUID
        User.users.add(user3);
    }

    public static User getUser(String userId) {
        if (User.users == null) {
            User.initUsers();
        }
        List<User> userList = User.users.stream().filter(user -> user.getId().equals(userId)).collect(Collectors.toList());
        if(userList.size() > 0){
            return userList.get(0);
        }
        return null;
    }

}