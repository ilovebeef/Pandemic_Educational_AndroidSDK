package gamePlayer_Controller;

public enum Roles {
    PLANNER, DISPATCHER, MEDIC, OPERATIONS_EXPERT, QUARANTINE_SPECIALIST, RESEARCHER, SCIENTIST;

    /*
    return random role
     */
    public static Roles getRandomRole() {
        return values()[(int) (Math.random() * values().length)];
    }

}
