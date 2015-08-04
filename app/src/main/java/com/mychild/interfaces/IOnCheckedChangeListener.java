package com.mychild.interfaces;

import com.mychild.model.StudentDTO;

/**
 * Created by Sandeep on 22-03-2015.
 */
public interface IOnCheckedChangeListener {
    public void checkedStateChanged(StudentDTO studentDTO, boolean isChecked);
}
