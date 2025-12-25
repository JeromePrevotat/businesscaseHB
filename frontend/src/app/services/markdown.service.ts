import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, from } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { marked } from 'marked';

@Injectable({ providedIn: 'root' })
export class MarkdownService {
  constructor(private http: HttpClient) {}

  loadMarkdown(path: string): Observable<string> {
    return this.http.get(path, { responseType: 'text' }).pipe(
      mergeMap(md => from(Promise.resolve(marked(md))))
    );
  }
}