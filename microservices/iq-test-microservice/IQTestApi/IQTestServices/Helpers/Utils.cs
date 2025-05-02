using System.Diagnostics;
using System.IO;

namespace IQTestServices.Helpers
{
    public static class Utils
    {
        private const string ModelRelativePath = "RavenAdvancedMatrices.zip";
        public static readonly string ModelPath = GetAbsolutePath(ModelRelativePath);

        private static string GetAbsolutePath(string relativePath)
        {
            var dataRoot = new FileInfo(typeof(Utils).Assembly.Location);
            Debug.Assert(dataRoot.Directory != null, "dataRoot.Directory != null");
            var assemblyFolderPath = dataRoot.Directory.FullName;

            var fullPath = Path.Combine(assemblyFolderPath, relativePath);

            return fullPath;
        }
    }
}