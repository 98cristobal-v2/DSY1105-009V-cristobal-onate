$path = 'C:\Users\Cris\Desktop\Ev. Parcial 4_Encargo_Estudiante_ DSY1105.pdf'
$bytes = [System.IO.File]::ReadAllBytes($path)
$text = [System.Text.Encoding]::ASCII.GetString($bytes)

# Extract PDF stream or text objects
# Let's search for BT ... ET or text strings in parentheses
$matches = [System.Text.RegularExpressions.Regex]::Matches($text, '\(([^)]+)\)')
foreach ($m in $matches) {
    $val = $m.Groups[1].Value
    # clean non-printable or weird characters
    $clean = $val -replace '[^\w\sáéíóúÁÉÍÓÚñÑ\.\,\-\:\/\?]', ''
    if ($clean.Length -gt 3 -and $clean -notmatch '^[0-9\s\.\,\-]+$') {
        $clean
    }
}
