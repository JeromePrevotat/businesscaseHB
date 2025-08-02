export interface User {
    readonly id: number | null;
    username: string;
    firstname: string;
    lastname: string;
    email: string;
    birthDate: Date;
    password: string;
    inscriptionDate: Date;
    accountValid: boolean;
    roleList: number[];
    banned: boolean;
    vehiculeList: number[];
    media_id: number;
    adressList: number[];
    reservationList: number[];
}
