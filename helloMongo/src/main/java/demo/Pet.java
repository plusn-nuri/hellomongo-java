package demo;



import org.bson.types.ObjectId;



public class Pet {
    public ObjectId id;
    public String name;
    public PetKind petKind;
    public int legCount;

    public Pet() {
    }

    public Pet(String name, PetKind kind, int legCount) {
        this.name = name;
        this.petKind = kind;
        this.legCount = legCount;
    }

    @Override
    public String toString() {
        return "[Named "+name+"][is a "+petKind+"]["+legCount+" legs]";
    }
}
