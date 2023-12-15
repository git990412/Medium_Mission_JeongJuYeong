import {combineReducers, configureStore} from "@reduxjs/toolkit";
import authReducer from "./feature/auth";
import storage from 'redux-persist/lib/storage';
import {persistReducer} from "redux-persist";

const reducers = combineReducers({
    auth: authReducer,
});

const persistConfig = {
    key: 'root', // localStorage key
    storage, // localStorage
};

const persistedReducer = persistReducer(persistConfig, reducers);

export default configureStore({
    reducer: persistedReducer,
    middleware: getDefaultMiddleware => getDefaultMiddleware({serializableCheck: false}),
});