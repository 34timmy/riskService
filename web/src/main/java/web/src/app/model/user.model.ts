
export interface UserModel {

    id: number;
    userName: string;
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    enabled: boolean;
    roles: string[];
    registered: string;
}
