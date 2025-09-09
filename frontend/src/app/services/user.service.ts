import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { API_URL } from '../utils/apiUrl';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(API_URL.USERS);
  }

  getUser(id:number): Observable<User> {
    return this.http.get<User>(`${API_URL.USERS}/${id}`);
  }

  createUser(user:Partial<User>): Observable<User> {
    return this.http.post<User>(`${API_URL.AUTH}/register`, user);
  }

  editUser(id: number, user: Partial<User>): Observable<User> {
    return this.http.put<User>(`${API_URL.USERS}/${id}`, user);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL.USERS}/${id}`);
  }

  changeUserPwd(user: User | null, passwords: { oldpwd: string; newpwd: string; newpwdconfirm: string }): Observable<User> {
    if (!user) {
      throw new Error('User is not authenticated');
    }
    const updated_user: User = user;
    updated_user.password = passwords.newpwd;
    return this.http.put<User>(`${API_URL.USERS}/${user.id}/change-password`, { user_id: user.id, oldpwd: passwords.oldpwd, newpwd: passwords.newpwd });
  }
}
