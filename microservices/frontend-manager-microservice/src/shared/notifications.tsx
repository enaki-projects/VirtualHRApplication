// Drop-in replacement for the unmaintained `react-notifications` package, whose
// transitions rely on ReactDOM.findDOMNode (removed in React 19). Same API:
// NotificationManager.success/info/warning/error(message, title?, timeOut?) and
// a <NotificationContainer/> mounted once near the app root.
import { useEffect, useState } from 'react';
import { Toast, ToastContainer } from 'react-bootstrap';

type Kind = 'success' | 'info' | 'warning' | 'error';
interface Note { id: number; kind: Kind; message: string; title?: string; timeOut: number }

const listeners = new Set<(notes: Note[]) => void>();
let notes: Note[] = [];
let nextId = 1;

function emit() { listeners.forEach((l) => l(notes)); }
function remove(id: number) { notes = notes.filter((n) => n.id !== id); emit(); }
function add(kind: Kind, message: string, title?: string, timeOut = 5000) {
  const note = { id: nextId++, kind, message, title, timeOut };
  notes = [...notes, note];
  emit();
  if (timeOut > 0) setTimeout(() => remove(note.id), timeOut);
}

export const NotificationManager = {
  success: (message: string, title?: string, timeOut?: number) => add('success', message, title, timeOut),
  info: (message: string, title?: string, timeOut?: number) => add('info', message, title, timeOut),
  warning: (message: string, title?: string, timeOut?: number) => add('warning', message, title, timeOut),
  error: (message: string, title?: string, timeOut?: number) => add('error', message, title, timeOut),
};

const bg: Record<Kind, string> = { success: 'success', info: 'info', warning: 'warning', error: 'danger' };

export function NotificationContainer() {
  const [items, setItems] = useState<Note[]>(notes);
  useEffect(() => {
    listeners.add(setItems);
    return () => { listeners.delete(setItems); };
  }, []);
  return (
    <ToastContainer position="top-end" className="p-3" style={{ zIndex: 2000, position: 'fixed' }}>
      {items.map((n) => (
        <Toast key={n.id} bg={bg[n.kind]} onClose={() => remove(n.id)}>
          {n.title ? (
            <Toast.Header><strong className="me-auto">{n.title}</strong></Toast.Header>
          ) : null}
          <Toast.Body className={n.kind === 'warning' || n.kind === 'info' ? '' : 'text-white'}>{n.message}</Toast.Body>
        </Toast>
      ))}
    </ToastContainer>
  );
}
