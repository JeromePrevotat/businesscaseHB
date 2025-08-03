export function toTitleCase(str: string): string {
    if (!str || str.length < 1) return '';
    if (!str || str.length == 1) return str.toUpperCase();
    return str.charAt(0).toUpperCase() + str.slice(1);
}