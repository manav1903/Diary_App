package com.nemosofts.library.pflockscreen.security.callbacks;

import com.nemosofts.library.pflockscreen.security.PFResult;

public interface PFPinCodeHelperCallback<T> {
    void onResult(PFResult<T> result);
}
