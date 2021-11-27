package io.metaloom.poc.db;

import java.time.LocalDateTime;

public interface PocCUDElement extends PocElement {

	PocElement setEditor(PocUser editor);

	PocElement setCreator(PocUser creator);

	LocalDateTime getEdited();

	PocElement setEdited(LocalDateTime edate);

	LocalDateTime getCreated();

	PocElement setCreated(LocalDateTime cdate);
}
