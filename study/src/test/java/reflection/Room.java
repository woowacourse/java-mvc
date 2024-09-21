package reflection;

public class Room {
    public final String name;
    protected final String address;
    private final int roomId;

    public Room(final String name, final String address, final int roomId) {
        this.name = name;
        this.address = address;
        this.roomId = roomId;
    }
}
