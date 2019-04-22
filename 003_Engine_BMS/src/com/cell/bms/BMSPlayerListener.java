package com.cell.bms;

import com.cell.bms.BMSFile.Note;

public interface BMSPlayerListener
{
	public void onBeat(BMSPlayer player, int beat_count);

	public void onDropNote(BMSPlayer player, Note note);
}
