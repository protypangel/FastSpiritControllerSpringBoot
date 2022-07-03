package org.fast.spirit.json.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import org.fast.spirit.annotation.custom.FastSpiritExample;
import org.fast.spirit.annotation.secondary.FastSpiritNote;
import org.fast.spirit.util.View;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@JsonView(View.Public.class)
@Getter
public class FastSpiritLeaf {
  private Map<String, String[]> notes;
  private String example;
  private String description;
  private String type;

  public FastSpiritLeaf(FastSpiritExample example, String typeName) {
    this.type = typeName;
    if (example != null) {
      if (example.notes().length > 0) {
        this.notes = new HashMap<>();
        Arrays.stream(example.notes()).forEach(this::AddNote);
      }
      if (!example.value().isEmpty()) this.example = example.value();
      if (!example.description().isEmpty()) this.description = example.description();
    }
  }

  private void AddNote(FastSpiritNote fastSpiritNote) {
    this.notes.put(fastSpiritNote.key(), fastSpiritNote.value());
  }
}
