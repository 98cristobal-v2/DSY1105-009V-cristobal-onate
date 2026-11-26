$path = 'C:\Users\Cris\Desktop\Ev. Parcial 4_Encargo_Estudiante_ DSY1105.pdf'
$bytes = [System.IO.File]::ReadAllBytes($path)
$text = [System.Text.Encoding]::ASCII.GetString($bytes)

$matches = [System.Text.RegularExpressions.Regex]::Matches($text, '\(([^)]+)\)')
foreach ($m in $matches) {
    $val = $m.Groups[1].Value
    if ($val.Length -gt 4 -and $val -match '[a-zA-Z]') {
        $val
    }
}
