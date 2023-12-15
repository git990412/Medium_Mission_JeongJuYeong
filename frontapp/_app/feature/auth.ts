import Member from '@/types/Member';
import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { RootState } from '../store';

// Type for our state
export interface AuthState {
    authState: boolean;
    member: Member;
}

// Initial state
const initialState: AuthState = {
    authState: false,
    member: { username: '' }
};
// Actual Slice
export const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        // Action to set the authentication status
        setAuthState(state, action: PayloadAction<{ state: boolean, member: Member }>) {
            state.authState = action.payload.state;
            state.member = action.payload.member
        }
    },
});

export const { setAuthState } = authSlice.actions;
export const selectAuthState = (state: RootState) => state.auth.authState;
export const selectUsername = (state: RootState) => state.auth.member.username;
export default authSlice.reducer;